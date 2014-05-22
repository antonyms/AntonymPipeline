#!/usr/bin/python
from pretraining.utils import *
import numpy as np
from pretraining.oov import readParam
from collections import defaultdict
import operator

posParser = {'n': 'NN', 'a': 'JJ', 's': 'JJ', 'v': 'VB', 'r': 'RB'}
pos = ['VB', 'NN', 'RB', 'JJ']

def load_gre_test(path):
    gre = []
    with open(path, 'r') as f:
        for row in f:
            row = row.split('::')
            answer = row[1].strip()
            row = row[0].split(':')
            target = row[0].strip()
            choice = row[1].strip().split()
            gre.append((target, choice, answer))
    return gre

types = defaultdict(float)
wrong = defaultdict(float)

def lookup_wordnet(gre):
    correct = 0
    answered = 0
    for q in gre:
        target, choice, answer = q
        target_ss = wn.synsets(target)
        if not target_ss:
            continue
        ants = []
        for ss in target_ss:
            types[ss.pos] += 1.0/len(target_ss)
            #types[ss.pos] += 1
            #print ss.pos
            ants += get_synset_ants_by_synsets(ss)
        ants = set(ants)

        candidate = []
        for c in choice:
            for cs in wn.synsets(c):
                #types[cs.pos] += 1
                if cs in ants:
                    candidate.append(c)
                    break
        if len(candidate) > 1:
            print('More than one answer:\n'+target+' : '+', '.join(candidate)+' :: '+answer)
            continue
            answered += 1
            if candidate[np.random.randint(len(candidate))] == answer:
                correct += 1
        elif len(candidate) == 1:
            answered += 1
            if candidate[0] == answer:
                correct += 1
        else:
            for ss in target_ss:
                wrong[ss.pos] += 1.0/len(target_ss)
            continue
            answered += 1
            if choice[np.random.randint(5)] == answer:
                correct += 1
            #print q
    return float(correct)/float(answered), float(correct)/len(gre)

def lookup(gre, ants):
    correct = 0
    answered = 0
    for q in gre:
        target, choice, answer = q
        if target not in ants:
            continue
        candidate = []
        for c in choice:
            if c in ants[target]:
                candidate.append(c)
        if len(candidate) > 1:
            c_candidate = []
            for c in choice:
                if c in ants and target in ants[c]:
                    c_candidate.append(c)
            if len(c_candidate) == 1:
                answered += 1
                if candidate[0] == answer:
                    correct += 1
                continue
            answered += 1
            if candidate[np.random.randint(len(candidate))] == answer:
                correct += 1
            continue
        if len(candidate) == 1:
            answered += 1
            if candidate[0] == answer:
                correct += 1
            continue
        # lookup from choices
        candidate = []
        for c in choice:
            if c in ants and target in ants[c]:
                candidate.append(c)
        if len(candidate) > 1:
            answered += 1
            if candidate[np.random.randint(len(candidate))] == answer:
                correct += 1
            continue
        if len(candidate) == 1:
            answered += 1
            if candidate[0] == answer:
                correct += 1
        else:
            answered += 1
            if choice[np.random.randint(5)] == answer:
                correct += 1

    return float(correct)/float(answered), float(correct)/len(gre)

def predict(gre, embed, T, oov_embed=None, tag=False):
    rand = True
    def score(a, b, T):
        return np.dot(T*a, b)
    pred = []
    for q in gre:
        target, choice, answer = q
        if tag:
            pos = get_question_pos(q)
            target = target+'#'+posParser[pos]
            choice = [c+'#'+posParser[pos] for c in choice]
            answer = answer+'#'+posParser[pos]
        if target not in embed:
            if oov_embed != None:
                if target not in oov_embed:
                    pred.append(None)
                    continue
                else:
                    target = oov_embed[target]
            else:
                pred.append(None)
                continue
        else:
            target = embed[target]
        min_sim = np.inf
        argmin = None
        for c in choice:
            co = c
            if c in embed:
                c = embed[c]
            elif False and oov_embed != None:
                if c in oov_embed:
                    c = oov_embed[c]
                else:
                    continue
            else:
                continue
            s = score(target, c, T)
            if s < min_sim:
                min_sim = s
                argmin = co
        if argmin != None:
            pred.append(argmin)
        else:
            print "XXX"
            if rand:
                pred.append(choice[random_choice()])
            else:
                pred.append(None)
    return pred

def random_choice():
    return np.random.randint(5)

def predict_avg(gre, embed, T, oov_embed=None):
    def score(a, b, T):
        return np.dot(T*a, b)
    pred = []
    n = len(embed)
    for q in gre:
        target, choice, answer = q
        if target not in embed[0]:
            if oov_embed != None:
                if target not in oov_embed:
                    pred.append(None)
                    continue
                else:
                    target = oov_embed[target]
            else:
                pred.append(None)
                continue
        else:
            target = [e[target] for e in embed]
            target = np.array(target).mean(0)
        min_sim = np.inf
        argmin = None
        for c in choice:
            co = c
            if c in embed[0]:
                c = [e[c] for e in embed]
                c = np.array(c).mean(0)
            elif oov_embed != None:
                if c in oov_embed:
                    c = oov_embed[c]
                else:
                    continue
            else:
                continue
            s = score(target, c, T)
            if s < min_sim:
                min_sim = s
                argmin = co
        if argmin != None:
            pred.append(argmin)
        else:
            pred.append(None)
    return pred

def score(gre, embed, T, oov_embed=None, AVG=False, tag=False):
    correct = 0
    answered = 0
    if AVG:
        pred = predict_avg(gre, embed, T, oov_embed)
    else:
        pred = predict(gre, embed, T, oov_embed, tag)
    for a, q in zip(pred, gre):
        target, choice, answer = q
        if a != None:
            answered += 1
            if tag:
                a = a.split('#')[0]
            if a == answer:
                correct += 1
    p = float(correct)/answered
    r = float(correct)/len(gre)
    print correct
    print answered
    print len(gre)
    return p, r, 2*p*r/(p+r)

def get_question_pos(question):
    target, choice, answer = question
    word = [target] + choice
    tag = defaultdict(float)
    for w in word:
        xpos = [x.pos for x in wn.synsets(w)]
        ss = wn.synsets(w)
        total = len(ss)
        count = []
        tags = []
        for x in ss:
            c= 1
            tags.append(x.pos)
            for xx in x.lemmas:
                n = xx.count()
                total += n
                c+= n
            count.append(c)
        count = [float(c)/total for c in count]
        for c,t in zip(count, tags):
            tag[t] += c
    tag['a'] += tag['s']
    del tag['s']
    return max(tag.iteritems(), key=operator.itemgetter(1))[0]

if __name__ == "__main__":
    embedding = False
    questionPath = '../data/test-data/testset.txt'
    gre = load_gre_test(questionPath)

    #ants, word = load_th_pair('mohammad/AntonymsLexicon-OppCats')

    dicPath = '../data/Rogets/antonym.txt'
    dicPath = '../data/WordNet-3.0/antonym.txt'
    dicPath = '../data/combine/antonym.txt'
    if False:
        ants, word = load_th(dicPath)
        '''
        p,r = lookup_wordnet(gre)
        print p,r,2*p*r/(p+r)
        print types
        print wrong
        exit()
        '''
        p,r= lookup(gre, ants)
        print p,r,2*p*r/(p+r)
        exit()

    pmfPath = "../data/bptf/roget_mm"
    matVocPath = "../data/bptf/roget_mm-58_voc"
    oovPath = "../data/bptf/gre_oov.txt"
    oov_embed, word = load_embedding(oovPath+".voc", oovPath)
    voc = load_dic(matVocPath)
    prec = []
    recall = []
    f1 = []
    for i in range(58,59):
        U, T, A, mu, alpha = readParam(pmfPath, i, 0)
        embed = dict(zip(voc, U))
        if not embedding:
            oov_embed = None
        p,r,f = score(gre, embed, T, oov_embed, tag=False)
        print i,p,r,f
        prec.append(p)
        recall.append(r)
        f1.append(f)
    i = np.argmax(np.array(recall))
    print i, prec[i], recall[i], f1[i]
    exit()

    embed = []
    for i in range(41):
        U, T, A, mu, alpha = readParam(pmfPath, i, 0)
        embed.append(dict(zip(voc, U)))
    print score(gre, embed, T, AVG=True)

    #print score(gre, embed, oov_embed)

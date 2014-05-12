import numpy as np
import re
import copy
import sys
from collections import defaultdict
from sklearn.feature_extraction.text import TfidfVectorizer

def cosine_sim(x, y):
    return np.dot(x, y) / np.sqrt(np.dot(x, x) * np.dot(y, y))

def preprocess_sim(sim):
    return sim/10 - 1

def load_dic(path):
    with open(path, 'r') as f:
        vec = f.readlines()
    vec = [x.strip() for x in vec]
    N = len(vec)
    assert N == len(set(vec)), "Vocabulary contains duplicates!"
    return vec

def save_dic(path, vec):
    with open(path, 'w') as f:
        f.write('\n'.join(vec))

def load_embedding(dicpath, embedpath):
    with open(dicpath, 'r') as f:
        word = f.readlines()
    word = [x.strip() for x in word]
    embed = {}
    with open(embedpath, 'r') as f:
        for i, row in enumerate(f):
            row = row.strip().split()
            row = [float(x) for x in row]
            row = np.array(row)
            embed[word[i]] = row
    return embed, word

def clean_th(ants,clean=True):
    new_ants = copy.deepcopy(ants)
    for w in ants:
        if clean:
            if not re.match(r"^[a-zA-Z\-]+$", w):
                del new_ants[w]
                continue
            temp = []
            for x in ants[w]:
                if re.match(r"^[a-zA-Z\-]+$", x):
                    temp.append(x)
        else:
            temp = ants[w]
        temp = set(temp)
        if w in temp:
            temp.remove(w)
        if not temp:
            del new_ants[w]
            continue
        new_ants[w] = temp
    return new_ants

def print_status(i):
    i += 1
    if i % 10 == 0:
        sys.stdout.write('.')
        if i % 1000 == 0:
            sys.stdout.write('\n')
            print i
    sys.stdout.flush()

def load_sim(simpath, voc=None, sample=True, prop=1e3):
    sim = {}
    with open(simpath, 'r') as f:
        count = 0
        for row in f:
            count += 1
    prop = float(prop) / count
    print 'sample probability: %f' % prop
    with open(simpath, 'r') as f:
        for row in f:
            if not sample or np.random.binomial(1, prop)==1:
                row = row.strip().split()
                a = row[0]
                b = row[1]
                if voc==None or a in voc and b in voc:
                    sim[(a,b)] = float(row[2])
    return sim

def get_voc_from_synant(antPath, synPath, gre):
    voc = set()
    with open(antPath, 'r') as f:
        data = f.readlines()
    for row in data:
        row = row.strip().split()
        target = row[0]
        if target in gre:
            for w in row[1:]:
                if w in gre:
                    voc.add(w)
                    voc.add(target)
    with open(synPath, 'r') as f:
        data = f.readlines()
    for row in data:
        row = row.strip().split()
        target = row[0]
        if target in gre:
            for w in row[1:]:
                if w in gre:
                    voc.add(w)
                    voc.add(target)
    return voc

def get_more_voc_from_ant(antPath, gre):
    voc = set()
    with open(antPath, 'r') as f:
        for row in f:
            row = row.strip().split()
            target = row[0]
            for w in row[1:]:
                if target in gre or w in gre:
                    voc.add(w)
                    voc.add(target)
    return voc

def get_more_voc_from_synant(antPath, synPath, gre):
    voc = set()
    with open(antPath, 'r') as f:
        data = f.readlines()
    for row in data:
        row = row.strip().split()
        target = row[0]
        for w in row[1:]:
            if target in gre or w in gre:
                voc.add(w)
                voc.add(target)
    with open(synPath, 'r') as f:
        data = f.readlines()
    for row in data:
        row = row.strip().split()
        target = row[0]
        for w in row[1:]:
            if target in gre or w in gre:
                voc.add(w)
                voc.add(target)
    temp = list(voc)
    voc = set()
    for w in temp:
        m = re.match(r"^[a-zA-Z\-]+$", w)
        if m:
            voc.add(w)
    return voc

def get_voc_tfidfdoc_from_synant(antPath, synPath, gre):
    voc = get_voc_from_synant(antPath, synPath, gre)
    doc = defaultdict(list)
    with open(antPath, 'r') as f:
        for row in f:
            row = row.strip().split()
            target = row[0]
            if target in voc:
                for w in row[1:]:
                    if w in voc:
                        doc[target].append(w)
    with open(synPath, 'r') as f:
        for row in f:
            row = row.strip().split()
            target = row[0]
            if target in voc:
                for w in row[1:]:
                    if w in voc:
                        doc[target].append(w)
    entries = doc.keys()
    doc = doc.values()
    doc = [' '.join(x) for x in doc]
    td = TfidfVectorizer(token_pattern=u'(?u)\\b[^ ]+\\b')
    doc = td.fit_transform(doc)
    items = td.get_feature_names()
    items = [str(x) for x in items]
    return voc, doc, entries, items

def get_voc_dic(voc):
    word2id = {}
    idx = 1
    for w in voc:
        word2id[w] = idx
        idx += 1
    return word2id, idx-1

def a_not_in_b(gre, th):
    not_th = set()
    for w in gre:
        if w not in th:
            not_th.add(w)
    return not_th

def load_th(path, clean=False):
    word = []
    ant = {}
    with open(path, 'r') as f:
        for row in f:
            words = row.strip().split('\t')
            if clean and not re.match(r"^[a-zA-Z\-]+$", words[0]):
                continue
            to = set([x for x in words[1:] if x != ''])
            if clean:
                to = [x for x in to if re.match(r"^[a-zA-Z\-]+$", x)]
                to = set(to)
            ant[words[0]] = to
            word += list(to)
    return ant, word

def save_th(path, ant):
    with open(path, 'w') as f:
        for w in ant:
            f.write(w+'\t'+'\t'.join(list(ant[w]))+'\n')

def save_pair_dict(path, d, sep=' '):
    with open(path, 'w') as f:
        for k in d:
            f.write(k[0]+sep+k[1]+sep+d[k]+'\n')

def load_entries(path):
    with open(path, 'r') as f:
        vec = f.readlines()
    vec = [x.strip().split()[0] for x in vec]
    vec = set(vec)
    return vec

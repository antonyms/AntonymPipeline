if (@ARGV ne 2) {die "args (2): 1st ordergraph, chiwidis clustering > chiwidis clustersi with feats";}
open(F,"<$ARGV[0]");
%h=();
while($in=<F>) {
 chomp($in);
 @a=split(/\t/,$in);
 if (defined $h{$a[0]}) {
   $h{$a[0]}.="\t".$a[1];
 } else {
   $h{$a[0]}=$a[1];
 }
}
close(F);
open(F,"<$ARGV[1]");
while($in=<F>) {
 %hh=();
 chomp($in);
 @a=split(/\t/,$in);
 @w=split(/, /,$a[2]);
 for($i=0;$i<@w;$i++) {
   if (defined $h{$w[$i]}) {
     @b=split(/\t/,$h{$w[$i]});
     for($j=0;$j<@b;$j++) {
       if (defined $hh{$b[$j]}) {
         $hh{$b[$j]}+=1;
       } else {
         $hh{$b[$j]}=1;
       }
     }
   } 
 }
 
 print "\n$a[0]\t$a[1]\t$a[2]\t";
 $count=0; 
 foreach $key (sort {$hh{$b} <=> $hh{$a} } keys %hh) {
   if ($count<100) {print " $key:$hh{$key}";}
   $count++;
 }
 
 
}
close(F); 
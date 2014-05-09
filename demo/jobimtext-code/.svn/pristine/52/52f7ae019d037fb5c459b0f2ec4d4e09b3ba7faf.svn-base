open(F,"<$ARGV[0]");
open(N,">$ARGV[0].nodes");
open(E,">$ARGV[0].edges_dup");


%wl=();
$wi=1;
while($in=<F>) {
  chomp($in);
  @a=split(/\t/,$in);
  if (!(defined $wl{$a[0]})) { $wl{$a[0]}=$wi;print N "$wi\t$a[0]\n";$wi++;};
  if (!(defined $wl{$a[1]})) { $wl{$a[1]}=$wi;print N "$wi\t$a[1]\n";$wi++;};
  $id1=$wl{$a[0]};
  $id2=$wl{$a[1]};
  
  if (($id1 ne $id2)&&($a[2]=~m/^[0-9]+$/)) {
    print E "$id1\t$id2\t$a[2]\n";
    print E "$id2\t$id1\t$a[2]\n";
  }  
  

}
close(F);
close(N);
close(E);
system("sort -u $ARGV[0].edges_dup |  sort -k1n -k3nr -t \"	\" > $ARGV[0].edges ");
system("rm $ARGV[0].edges_dup");
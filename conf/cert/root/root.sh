topdir=/sdk/tmp/spring/solr-pagination/conf/cert
currdir=$topdir/root
mkdir -p $currdir/rootCA
mkdir -p $currdir/rootCA/{private,certs,crl,newcerts}
touch $currdir/rootCA/private/.rand
touch $currdir/rootCA/index.txt
touch $currdir/rootCA/serial
echo "01" > $currdir/rootCA/serial 

export RANDFILE="$currdir/rootCA/private/.rnd"; 
openssl genrsa -des3 -out $currdir/rootCA/private/cakey.pem 1024
openssl req -new -x509 -key $currdir/rootCA/private/cakey.pem -out $currdir/rootCA/cacert.pem -extensions v3_ca -passin pass:rootca -subj /C=CN/ST=SH/L=SH/O=ZhiSheng\ Inc/OU=Cert/CN=RootCA -days 3650

#openssl rsa -in /sdk/tmp/ca/root/rootCA/private/cakey.pem -text -noout
#openssl x509 -in /sdk/tmp/ca/root/rootCA/cacert.pem -text -noout

#following two steps can be done agency as well.
#openssl genrsa -des3 -out agency.key 1024
#openssl req -new -key agency.key -out agency.csr

openssl ca -in $topdir/agency/agency.csr -extensions v3_ca -config $currdir/openssl.cnf -passin pass:rootca -days 3650

send $currdir/rootCA/newcerts/01.pem to agency ( agency.key as well if we generated the key for agency )

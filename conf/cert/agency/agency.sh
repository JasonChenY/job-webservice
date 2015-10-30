topdir=/sdk/tmp/spring/solr-pagination/conf/cert
currdir=$topdir/agency
mkdir -p $currdir/rootCA
mkdir -p $currdir/rootCA/{private,certs,crl,newcerts}
touch $currdir/rootCA/private/.rand
touch $currdir/rootCA/index.txt
touch $currdir/rootCA/serial
echo "01" > $currdir/rootCA/serial

#if root generate key and request for us, bypassing following two steps
export RANDFILE="$currdir/rootCA/private/.rnd"; 
openssl genrsa -des3 -out $currdir/rootCA/private/cakey.pem 1024 
openssl req -new -key $currdir/rootCA/private/cakey.pem -out $currdir/agency.csr -extensions v3_ca -passin pass:agency -subj /C=CN/ST=SH/L=SH/O=ZhiSheng\ Inc/OU=Cert\ Agency/CN=AgencyCA -days 3650
#send agency.csr to root CA for sign

#else rootCA will send us our key and certificate
#cp $topdir/root/agency.key $topdir/agency/rootCA/private/cakey.pem
#endif

#install the certification from rootCA to agency's store.
cp $topdir/root/rootCA/newcerts/01.pem  $currdir/rootCA/cacert.pem


#when receive tiaonr's request, should add corresponding items in openssl.cnf for v3_req (even though that info already exist in csr)
#openssl ca -in $topdir/tiaonr/tiaonr.csr -config $currdir/openssl.cnf -passin pass:agency -extensions v3_req -extensions usr_cert
openssl ca -in $topdir/tiaonr/tiaonr.csr -config $currdir/openssl.cnf -passin pass:agency -extensions v3_req -days 3650

#send $currdir/rootCA/newcerts/01.pem to tiaonr


#resign a request
#revoke the certificate firstly
openssl ca -revoke $currdir/rootCA/newcerts/01.pem -config $currdir/openssl.cnf -passin pass:agency
#sign again ( it will appear with new seq number )
openssl ca -in $topdir/tiaonr/tiaonr.csr -config $currdir/openssl.cnf -passin pass:agency -extensions v3_req


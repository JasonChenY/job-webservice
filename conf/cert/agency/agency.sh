mkdir -p rootCA
mkdir -p rootCA/{private,certs,crl,newcerts}
touch rootCA/private/.rnd
touch rootCA/index.txt
touch rootCA/serial
echo "01" > rootCA/serial 


#if root generate key and request for us, bypassing following two steps
export RANDFILE="rootCA/private/.rnd"; 
openssl genrsa -des3 -out /sdk/tmp/ca/agency/rootCA/private/cakey.pem 1024 
openssl req -new -key /sdk/tmp/ca/agency/rootCA/private/cakey.pem -out agency.csr -extensions v3_ca -passin pass:agency -subj /C=CN/ST=SH/L=SH/O=ZhiSheng\ Inc/OU=Cert\ Agency/CN=AgencyCA
#send agency.csr to root CA for sign

#else rootCA will send us our key and certificate
#cp /sdk/tmp/ca/root/agency.key /sdk/tmp/ca/agency/rootCA/private/cakey.pem
#endif

#install the certification from rootCA to agency's store.
cp /sdk/tmp/ca/root/rootCA/newcerts/01.pem  /sdk/tmp/ca/agency/rootCA/cacert.pem


#when receive tiaonaer's request, should add corresponding items in openssl.cnf for v3_req (even though that info already exist in csr)
#openssl ca -in /sdk/tmp/ca/tiaonaer/tiaonaer.csr -config openssl.cnf -passin pass:agency -extensions v3_req -extensions usr_cert
openssl ca -in /sdk/tmp/ca/tiaonaer/tiaonaer.csr -config openssl.cnf -passin pass:agency -extensions v3_req

#send /sdk/tmp/ca/agency/rootCA/newcerts/01.pem to tiaonaer 


#resign a request
#revoke the certificate firstly
openssl ca -revoke rootCA/newcerts/01.pem -config openssl.cnf -passin pass:agency
#sign again ( it will appear with new seq number )
openssl ca -in /sdk/tmp/ca/tiaonaer/tiaonaer.csr -config openssl.cnf -passin pass:agency -extensions v3_req


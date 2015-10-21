#/bin/bash
prog=`basename $0`
dir=`dirname $0`
dir=`readlink -e $dir`
destdir=$dir/target
if [ $# -eq 0 ]; then
   echo "usage: $prog (create|build) [destdir]"
   exit
elif [ $# -eq 2 ]; then
   destdir=$2
   if [ ! -d $destdir ]; then
      echo "dest dir $destdir not exist, confirm!"
      exit
   fi
fi

if [ $1 = "create" ]; then
   cd $destdir
   rm -rf jobapp
   cordova create jobapp com.tiaonaer.jobapp Tiaonaer 
   cd jobapp 

   echo "download plugins"
   cordova platform add android
   cordova plugin add cordova-plugin-inappbrowser

   rm -rf www
   ln -sf $dir/src/main/webapp www

   cordova build android
elif [ $1 = "build" ]; then
   cd $destdir/jobapp
   cordova build android
else
   echo "usage: $prog (create|build)"
   exit
fi
    

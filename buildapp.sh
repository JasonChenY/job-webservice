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
   cordova create jobapp com.tiaonr.jobapp 跳哪儿
   cd jobapp 

   echo "download plugins"
   cordova platform add android
   cordova plugin add cordova-plugin-inappbrowser

   # following stuff can be removed with official certificate ?
   # cordova plugin add cordova-plugin-sslcertificatechecker
   # there is one bug to be fixed manully Log->LOG.
   # cordova plugin add cordova-plugin-certificates

   rm -rf www
   ln -sf $dir/src/main/webapp www

   cp $dir/conf/app/res/mipmap-mdpi/icon.png $destdir/jobapp/platforms/android/res/drawable-mdpi/
   cp $dir/conf/app/res/mipmap-hdpi/icon.png $destdir/jobapp/platforms/android/res/drawable-hdpi/
   cp $dir/conf/app/res/mipmap-xhdpi/icon.png $destdir/jobapp/platforms/android/res/drawable-xhdpi/
   cp $dir/conf/app/res/mipmap-xhdpi/icon.png $destdir/jobapp/platforms/android/res/drawable/

   cordova build android
elif [ $1 = "build" ]; then
   cd $destdir/jobapp
   cordova build android
else
   echo "usage: $prog (create|build)"
   exit
fi
    

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
   cordova plugin add cordova-plugin-splashscreen

   # following stuff can be removed with official certificate ?
   # cordova plugin add cordova-plugin-sslcertificatechecker
   # there is one bug to be fixed manully Log->LOG.
   # cordova plugin add cordova-plugin-certificates

   rm -rf www
   #ln -sf $dir/src/main/webapp www

   mkdir -p www/static
   cp -r $dir/target/jobws/locales www
   cp $dir/target/jobws/index.html www
   cp -r $dir/target/jobws/static/images www/static
   cp $dir/target/jobws/static/lib.min.js www/static
   cp $dir/target/jobws/static/jobws.min.js www/static
   cp $dir/target/jobws/static/jobws.min.css www/static

   cp $dir/conf/app/res/mipmap-mdpi/icon.png $destdir/jobapp/platforms/android/res/drawable-mdpi/
   cp $dir/conf/app/res/mipmap-hdpi/icon.png $destdir/jobapp/platforms/android/res/drawable-hdpi/
   cp $dir/conf/app/res/mipmap-xhdpi/icon.png $destdir/jobapp/platforms/android/res/drawable-xhdpi/
   #cp $dir/conf/app/res/mipmap-xhdpi/icon.png $destdir/jobapp/platforms/android/res/drawable/

   cp $dir/conf/app/res/drawable-land-ldpi/screen.png $destdir/jobapp/platforms/android/res/drawable-land-ldpi
   cp $dir/conf/app/res/drawable-land-mdpi/screen.png $destdir/jobapp/platforms/android/res/drawable-land-mdpi
   cp $dir/conf/app/res/drawable-land-hdpi/screen.png $destdir/jobapp/platforms/android/res/drawable-land-hdpi
   cp $dir/conf/app/res/drawable-land-xhdpi/screen.png $destdir/jobapp/platforms/android/res/drawable-land-xhdpi
   cp $dir/conf/app/res/drawable-land-xxhdpi/screen.png $destdir/jobapp/platforms/android/res/drawable-land-xxhdpi
   cp $dir/conf/app/res/drawable-land-xxxhdpi/screen.png $destdir/jobapp/platforms/android/res/drawable-land-xxxhdpi

   cp $dir/conf/app/res/drawable-port-ldpi/screen.png $destdir/jobapp/platforms/android/res/drawable-port-ldpi
   cp $dir/conf/app/res/drawable-port-mdpi/screen.png $destdir/jobapp/platforms/android/res/drawable-port-mdpi
   cp $dir/conf/app/res/drawable-port-hdpi/screen.png $destdir/jobapp/platforms/android/res/drawable-port-hdpi
   cp $dir/conf/app/res/drawable-port-xhdpi/screen.png $destdir/jobapp/platforms/android/res/drawable-port-xhdpi
   cp $dir/conf/app/res/drawable-port-xxhdpi/screen.png $destdir/jobapp/platforms/android/res/drawable-port-xxhdpi
   cp $dir/conf/app/res/drawable-port-xxxhdpi/screen.png $destdir/jobapp/platforms/android/res/drawable-port-xxxhdpi

   cordova build android
elif [ $1 = "build" ]; then
   cd $destdir/jobapp
   cp $dir/target/jobws/index.html www
   cp -r $dir/target/jobws/static/images www/static
   cp $dir/target/jobws/static/lib.min.js www/static
   cp $dir/target/jobws/static/jobws.min.js www/static
   cp $dir/target/jobws/static/jobws.min.css www/static
   cordova build android
else
   echo "usage: $prog (create|build)"
   exit
fi
    

#!/bin/sh
leftbit="image_url["
middlebit="] = 'images/"
endbit="';"
cd war/images/
ls *.png > images
count=-1
while read line
do count=$(($count+1));
echo $leftbit$count$middlebit$line$endbit;
done < "images"
rm images

#!/bin/bash

# Jmeter path
JMETER_PATH=/home/ubuntu/jmeter

# Obtain GET parameters
saveIFS=$IFS
IFS='=&'
parm=($QUERY_STRING)
IFS=$saveIFS

# Create an associative array 'get-params'
declare -a getarray
for ((i=0; i<${#parm[@]}; i+=2))
do
    getarray[${parm[i]}]=${parm[i+1]}
done

#echo param: $param
#echo getarray: $getarray
#echo getarray: ${getarray[loaddriver]}

if [ ! -z "${getarray[loaddriver]}"  ]
then
    if [ "${getarray[loaddriver]}" == "on" ]
    then
        ${JMETER_PATH}/jmeterld.sh start  > /dev/null 2>&1
    elif [ "${getarray[loaddriver]}" == "off" ]
    then
        ${JMETER_PATH}/jmeterld.sh stop  > /dev/null 2>&1
    fi
fi



echo "Content-type: text/html"
echo ""

echo '<html>'
echo '<head>'
echo '<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">'
echo '<title>Load Driver</title>'
echo '</head>'
echo '<body>'
echo '<h1>Load Driver</h1>'
#echo '<p>'
#echo '$QUERY_STRING: '
#echo $QUERY_STRING
#echo '</p>'
echo -n 'Load Driver is '
echo -n '<span style="font-weight: bold">'
echo -n $( ${JMETER_PATH}/jmeterld.sh status  )
echo '</span>'
echo '<form>'
echo '<label>'
echo -n 'Turn load driver &ensp;'
echo -n 'on <input type="radio" name="loaddriver" value="on"'
[ "${getarray[loaddriver]}" == "on" ] && echo -n " checked "
echo -n '> '
echo -n 'off <input type="radio" name="loaddriver" value="off" '
[ "${getarray[loaddriver]}" == "off" ] && echo -n " checked "
echo '> '
echo '</label>'
echo '<input type="submit" value="Submit">'
echo '</form>'

if [ ! -z "${getarray[loaddriver]}" ]; then 
   echo -n "<h4>Please <a href=\"#\" onclick=\"window.location = window.location.protocol + '//' + window.location.hostname + ':8080';\">"
   echo -n 'go back</a> and check the application logs (orders log, matches log, requests log) to verify the load driver status'
   echo '</h4>'
fi

echo -n "<h3><a href=\"#\" onclick=\"window.location = window.location.protocol + '//' + window.location.hostname + ':8080';\">"
echo 'Home</a></h3>'
echo '</body>'
echo '</html>'

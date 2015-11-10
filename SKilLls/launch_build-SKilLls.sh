cd ../scala/bin
new=$(pwd)
case ":${PATH:=$new}:" in
    *:$new:*)  ;;
    *) PATH="$new:$PATH"  ;;
esac
cd ../../SKilLls
ant -f build-SKilLls.xml clean
ant -f build-SKilLls.xml


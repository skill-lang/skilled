#!/bin/bash
cd ../../scala/bin
chmod +x *
new=$(pwd)
case ":${PATH:=$new}:" in
    *:$new:*)  ;;
    *) PATH="$new:$PATH"  ;;
esac
cd ../../SKilLEd/de.unistuttgart.iste.ps.skillls
ant -f build-SKilLls.xml

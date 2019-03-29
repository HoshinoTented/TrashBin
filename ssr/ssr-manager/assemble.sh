#!/bin/bash

fileName="ssrmanager.jar"

gradle assemble
cp build/libs/${fileName} ${HOME}/.local/share/ssr-manager/${fileName}
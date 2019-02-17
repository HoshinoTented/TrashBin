#!/usr/bin/env bash

work_dir="~/Documents/Projects/Unknown"

mkdir -p ${work_dir}
mkdir -p ~/.local/bin/
mkdir -p ~/.emacs.d/

cd ${work_dir}

cp ./init.el ~/.emacs.d/

# repositories
# sudo add-apt-repository ppa:noobslab/themes

sudo apt update

# common tools
sudo apt install git curl emacs24 zip

# flatabulous unity-tweak-tool
# sudo apt install flatabulous-theme unity-tweak-tool

# jdks
sudo apt install openjdk-8-jdk openjdk-8-source openjfx openjfx-source

# repository
echo "Cloning repository"
git clone https://github.com/HoshinoTented/TrashBin.git

cd TrashBin

# hoshino's terminal configurations
init_sh=".hoshino_init.sh"
echo "Configuring ${init_sh}"

ln ${init_sh} ~/
echo -e "\nsource ${init_sh}" >> ~/.bashrc

source ~/.bashrc

# ssr
echo "Configuring ShadowsockR"

ssr_install="~/.local/bin/ssr"
# curl -sSL https://github.com/the0demiurge/CharlesScripts/blob/master/charles/bin/ssr?raw=true > ${ssr_install}
# chmod +x ${ssr_install}
ln ./ssr ${ssr_install}
ssr install

echo "Please configure your ssr"
ssr config
ssr start

# haskell-stack
echo "Configuring Haskell-Stack"

stack_config="~/.stack"
mkdir ${stack_config}
curl -sSL https://get.haskellstack.org/ | sh
cp ./haskellstack/config.yaml ${stack_config}/config.yaml
stack setup #--resolver=ghc-8.6.3

## haskell packages
stack install vector

# sdkman | install kotlin, gradle
echo "Configuring SDKMAN!"
useProxy

curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

sdk install kotlin
sdk install gradle

removeProxy

# Chrome
echo "Installing Chrome"
useProxy

chrome_install="/tmp/chrome.deb"
wget "https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb" -O ${chrome_install}
sudo dpkg -i ${chrome_install}

removeProxy
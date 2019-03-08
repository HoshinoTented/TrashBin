#!/usr/bin/env bash

export work_dir="~/Documents/Projects/Unknown"
export EDITOR="gedit"

function sayHello() {
    echo "QWQ"
}

function predo() {
    mkdir -p ${work_dir}
    mkdir -p ~/.local/bin/
    mkdir -p ~/.emacs.d/
}

function emacsScript() {
    cp ./init.el ~/.emacs.d/
}

# repositories
# sudo add-apt-repository ppa:noobslab/themes

function apts() {
    sudo apt update
    sudo apt install git curl zip
}

# flatabulous unity-tweak-tool
# sudo apt install flatabulous-theme unity-tweak-tool

function jdks() {
    sudo apt install openjdk-8-jdk openjdk-8-source openjfx openjfx-source
}


function cloneRepo() {
    echo "Cloning repository"
    git clone https://github.com/HoshinoTented/TrashBin.git
    # cd TrashBin
}

function initShell() {
    init_sh=".hoshino_init.sh"
    echo "Configuring ${init_sh}"

    ln ${init_sh} ~/
    echo -e "\nsource ${init_sh}" >> ~/.bashrc

    source ~/.bashrc
}

function shadowsocksr() {
    echo "Configuring ShadowsockR"

    ssr_install="~/.local/bin/ssr"
    # curl -sSL https://github.com/the0demiurge/CharlesScripts/blob/master/charles/bin/ssr?raw=true > ${ssr_install}
    ln ./ssr/ssr ${ssr_install}
    chmod +x ${ssr_install}
    ssr install

    echo "Please configure your ssr"
    ssr config
    ssr start

    export ALL_PROXY="socks5://localhost:1080"
}

function haskellstack() {
    echo "Configuring Haskell-Stack"

    stack_config="~/.stack"
    mkdir ${stack_config}
    curl -sSL https://get.haskellstack.org/ | sh
    cp ./haskellstack/config.yaml ${stack_config}/config.yaml
    stack setup #--resolver=ghc-8.6.3

    ## haskell packages
    stack install vector
}

function sdkman() {
    echo "Configuring SDKMAN!"

    curl -s "https://get.sdkman.io" | bash
    source "$HOME/.sdkman/bin/sdkman-init.sh"

    sdk install kotlin
    sdk install gradle
}

function chrome() {
    echo "Installing Chrome"

    chrome_install="/tmp/chrome.deb"
    wget "https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb" -O ${chrome_install}
    sudo dpkg -i ${chrome_install}
}

function all() {
    for name in predo apts jdks cloneRepo shadowsocksr haskellstack chrome sdkman initShell
    do
        ${name}
    done
}
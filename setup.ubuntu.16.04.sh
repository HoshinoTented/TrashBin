#!/usr/bin/env bash

repo_name="TrashBin"
work_dir="${HOME}/Documents/Projects/Unknown"
repo_dir="${work_dir}/${repo_name}"
export EDITOR="gedit"

function sayHello() {
    echo "QWQ"
}

function predo() {
    mkdir -p ${repo_dir}
    mkdir -p ${HOME}/.local/bin/
}

function emacsScript() {
    mkdir -p ${HOME}/.emacs.d/
    cp ${repo_dir}/init.el ${HOME}/.emacs.d/
}

# repositories
# sudo add-apt-repository ppa:noobslab/themes

function apts() {
    sudo apt update
    sudo apt install git curl zip htop
}

# flatabulous unity-tweak-tool
# sudo apt install flatabulous-theme unity-tweak-tool

function jdks() {
    sudo apt install openjdk-8-jdk openjdk-8-source openjfx openjfx-source
}


function cloneRepo() {
    echo "Cloning repository"
    git clone https://github.com/HoshinoTented/${repo_name}.git ${repo_dir}
    # cd TrashBin
}

function initShell() {
    init_sh=".hoshino_init.sh"
    echo "Configuring ${init_sh}"

    ln ${repo_dir}/${init_sh} ${HOME}/
    echo -e "\nsource ${HOME}/${init_sh}" >> ${HOME}/.bashrc
}

function shadowsocksr() {
    echo "Configuring ShadowsockR"

    ssr_install="${HOME}/.local/bin/ssr"
    # curl -sSL https://github.com/the0demiurge/CharlesScripts/blob/master/charles/bin/ssr?raw=true > ${ssr_install}
    ln ${repo_dir}/ssr/ssr ${ssr_install}
    chmod +x ${ssr_install}
    ssr install

    echo "Please configure your ssr"
    ssr config
    ssr start

    export ALL_PROXY="socks5://localhost:1080"
}

function haskellstack() {
    echo "Configuring Haskell-Stack"

    stack_config="${HOME}/.stack"
    mkdir ${stack_config}
    curl -sSL https://get.haskellstack.org/ | sh
    cp ${repo_dir}/haskellstack/config.yaml ${stack_config}/config.yaml
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
export EDITOR="emacsclient -nw"
alias 给本小姐=sudo
alias 猫=cat
alias 喵=bat
alias ec="emacsclient -nw"

function useProxy() {
    export ALL_PROXY=socks5://localhost:1080
}

function removeProxy() {
    export ALL_PROXY=""
}

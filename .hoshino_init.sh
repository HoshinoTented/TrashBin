export EDITOR="emacsclient -nw"
alias 给本小姐=sudo
alias 猫=cat
alias 喵=bat
alias 爆裂吧=rm
alias 显现吧=ls
alias 终结吧=exit
alias ec="emacsclient -nw"

# Haskell
alias ghci="stack ghci"
alias ghc="stack ghc"
alias runghc="stack runghc"

# QueDe Julia
alias julia="/opt/julia/bin/julia"

function useProxy() {
    export ALL_PROXY="socks5://localhost:1080"
}

function removeProxy() {
    export ALL_PROXY=""
}

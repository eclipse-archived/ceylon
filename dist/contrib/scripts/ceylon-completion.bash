# bash completion support for ceylon
# 
# To use this:
# 1. Copy the file somewhere (e.g. ~/.ceylon/ceylon-completion.bash)
# 2. Add the following to your .bashrc
#      source ~/.ceylon/ceylon-completion.bash
#

__ceylon_args() {
    for var in "${COMP_WORDS[@]}"
    do
        echo -n "${var} "
    done
}

__ceylon_complete() {
    COMPREPLY=()
    COMPREPLY=( $( $__ceylon_cmd bash-completion --cword=${COMP_CWORD} -- $(__ceylon_args) ) )
}

__ceylon_cmd=$1
: ${__ceylon_cmd:="ceylon"}

complete -o nospace -F __ceylon_complete $__ceylon_cmd
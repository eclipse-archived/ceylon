# bash completion support for ceylon
# 
# To use this:
# 1. Copy the file somewhere (e.g. ~/.ceylon/ceylon-completion.bash)
# 2. Add the following to your .bashrc
#      source ~/.ceylon/ceylon-completion.bash
#

__cey_args() {
    for var in "${COMP_WORDS[@]}"
    do
        echo -n "${var} "
    done
}

__cey_complete() {
    COMPREPLY=()
    COMPREPLY=( $( $__cey_cmd bash-completion --cword=${COMP_CWORD} -- $(__cey_args) ) )
}

__cey_cmd=$1
: ${__cey_cmd:="cey"}

complete -o nospace -F __cey_complete $__cey_cmd
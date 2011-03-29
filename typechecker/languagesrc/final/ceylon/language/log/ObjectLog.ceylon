shared extension class ObjectLog(Object this) {
    shared Log log {
        return Log(defaultLogChannel, this.type.name);
    }
}
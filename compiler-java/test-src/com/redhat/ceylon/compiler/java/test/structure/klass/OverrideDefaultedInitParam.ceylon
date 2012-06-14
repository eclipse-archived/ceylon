class OverrideDefaultedInitParam(id="") {
    String id;
}

class OverrideDefaultedInitParamSub(String id="") extends OverrideDefaultedInitParam(id) {}
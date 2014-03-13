shared variable String expressionRecoveryVariableValueInit = asdfVariableValueInit();
void expressionRecoveryVariableValueInit_main(SequenceBuilder<Integer> sb) {
    sb.append(1);
    expressionRecoveryVariableValueInit = "";
    sb.append(2);
}
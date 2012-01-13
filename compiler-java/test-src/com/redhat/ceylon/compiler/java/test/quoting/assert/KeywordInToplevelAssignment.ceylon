@nomodel
@error
variable Integer assert := 0;

@nomodel
void keywordInToplevelAssignment() {
    @error
    assert := 1;
}

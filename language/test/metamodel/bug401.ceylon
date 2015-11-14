@test
see (`value system.milliseconds`, 
     `function process.write`,
     `class system`)
shared void bug401() {
    assert(exists SeeAnnotation see = `function bug401`.annotations<SeeAnnotation>().first,
        see.programElements == [`value system.milliseconds`, 
                                `function process.write`,
                                `class system`]);
}
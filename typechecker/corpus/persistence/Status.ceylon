shared class Status() 
of draft | submitted | shipped | closed 
extends Case() {}

doc "A draft order being edited"
charColumnValue "DR"
shared object draft extends Status() {}

doc "A submitted order ready for
     processing"
charColumnValue "SU"
shared object submitted extends Status() {}

doc "An order that has been shipped"
charColumnValue "SH"
shared object shipped extends Status() {}

doc "An order that has been delivered"
charColumnValue "CL"
shared object closed extends Status() {}

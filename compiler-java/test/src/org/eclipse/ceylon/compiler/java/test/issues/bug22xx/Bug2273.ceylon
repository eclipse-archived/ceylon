class Bug2273 {
    shared new create() {} 
}

Bug2273? bug2273()
        => if (true)
        then Bug2273.create()
        else null;
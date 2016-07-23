Array<Element> array<Element>(Array<Element> arr)
        => Array(object satisfies {Element*} { iterator() => arr.iterator(); });
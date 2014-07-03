String center(String content, Integer size) {
    value padding = size-content.size;
    value paddingBefore = padding/2;
    value paddingAfter = padding-paddingBefore;
    return " ".repeat(paddingBefore) + content + 
            " ".repeat(paddingAfter);
}

class Cell({String*} content) {
    shared actual String string
            => String(expand(content));
}

class Row({Cell*} cell) {
    shared Cell[] cells = cell.sequence();
    shared actual String string {
        return "| " + "|".join {
            for (cell in cells) center(cell.string, 20)
        } + "|";
    }
}

class Table(String title, Row header, {Row*} rows) {
    shared actual String string {
        value size = header.cells.size*21+1;
        return "\n".join {
            center(title, size),
            center("-".repeat(title.size), size),
            header.string.replace("|", " "),
            "-".repeat(size),
            for (row in rows) 
                row.string + "\n" + 
                "-".repeat(row.cells.size*21+1)
        };
    }
}
return $_String(this.substring((this.charCodeAt(0)&0xfc00) === 0xd800 ?2:1));

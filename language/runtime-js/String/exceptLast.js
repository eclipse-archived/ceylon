return $_String(this.substring(0, (this.charCodeAt(this.length-2)&0xfc00) === 0xd800 ?this.length-2:this.length-1));

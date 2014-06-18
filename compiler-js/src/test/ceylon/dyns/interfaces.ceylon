import check { check, fail }

dynamic DataHolder {
  shared formal dynamic data;
}

//See #362
dynamic Node satisfies DataHolder {
  shared formal Array<Node> children;
}

Node createNode(Node? k2) {
  dynamic {
    dynamic k1 = value{data="A child.";children=[];};
    dynamic r = value{
      data=value{a=1;b="2";};
      children=value{k1};
    };
    if (k2 exists) {
      r.children.push(k2);
    }
    return r;
  }
}

void testDynamicInterfaces() {
  Object o = createNode(null);
  if (is Node n=o) {
    check(true, "Dynamic interfaces #1");
    check(n.children.size==1, "Dynamic interfaces #2");
    //TODO should we really expect n.children[0] to be a Node?
    if (exists k=n.children[0]) {
      check(true, "Dynamic interfaces #3");
      check(k.children.size==0, "Dynamic interfaces #4");
      dynamic {
        check(k.data=="A child.", "Dynamic interfaces #5");
      }
    } else {
      fail("Dynamic interfaces #3");
    }
    check(!n.children[1] exists, "Dynamic interfaces #6");
  } else {
    fail("Dynamic interfaces #1");
  }
}

import java.awt { Color }

@test
shared void bug365() {
  value x = [ [1, Color(0)] ];
  value y = { for(e in x) e[1] };
  print(y);
}
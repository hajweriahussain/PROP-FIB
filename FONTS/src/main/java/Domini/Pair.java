package Domini;

public class Pair<T1, T2> {
    public T1 clau;
    public T2 valor;

    public Pair(T1 clau, T2 valor) {
        this.clau = clau;
        this.valor = valor;
    }

    public T1 getClau() {
        return clau;
    }

    public void setClau(T1 clau) {
        this.clau = clau;
    }

    public String clauToString() {
        return clau.toString();
    }

    public T2 getValor() {
        return valor;
    }

    public void setValor(T2 valor) {
        this.valor = valor;
    }

    public String valorToString() {
        return valor.toString();
    }

    @Override
    public String toString() {
        return "(" + clau.toString() + "," + valor.toString() + ')';
    }
}

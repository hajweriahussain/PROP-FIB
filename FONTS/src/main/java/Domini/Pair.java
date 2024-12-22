package Domini;

/**
 * Aquesta classe representa un parell de valors genèrics. El primer valor es
 * denomina "clau" i el segon "valor". Es tracta d'una estructura de dades que
 * emmagatzema dos objectes de tipus genèric T1 i T2, respectivament.
 *
 * @param <T1> Tipus de la clau.
 * @param <T2> Tipus del valor.
 */
public class Pair<T1, T2> {

    /** Clau del parell. */
    public T1 clau;

    /** Valor del parell. */
    public T2 valor;

    /**
     * Constructor que inicialitza un nou parell amb els valors proporcionats per
     * paràmetre.
     *
     * @param clau El valor de la clau.
     * @param valor El valor associat a la clau.
     */
    public Pair(T1 clau, T2 valor) {
        this.clau = clau;
        this.valor = valor;
    }

    /**
     * Retorna la clau del parell.
     *
     * @return La clau del parell.
     */
    public T1 getClau() {
        return clau;
    }

    /**
     * Estableix una nova clau per al parell.
     *
     * @param clau La nova clau a assignar al parell.
     */
    public void setClau(T1 clau) {
        this.clau = clau;
    }

    /**
     * Retorna una representació en cadena de la clau.
     *
     * @return Una cadena que representa la clau.
     */
    public String clauToString() {
        return clau.toString();
    }

    /**
     * Retorna el valor associat a la clau en el parell.
     *
     * @return El valor del parell.
     */
    public T2 getValor() {
        return valor;
    }

    /**
     * Estableix un nou valor per al parell.
     *
     * @param valor El nou valor a assignar al parell.
     */
    public void setValor(T2 valor) {
        this.valor = valor;
    }

    /**
     * Retorna una representació en cadena del valor.
     *
     * @return Una cadena que representa el valor.
     */
    public String valorToString() {
        return valor.toString();
    }

    /**
     * Retorna una representació en cadena del parell, format com "(clau,valor)".
     *
     * @return Una cadena que representa el parell.
     */
    @Override
    public String toString() {
        return "(" + clau.toString() + "," + valor.toString() + ')';
    }
}

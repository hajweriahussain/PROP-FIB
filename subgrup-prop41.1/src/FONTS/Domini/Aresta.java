package Domini;

public class Aresta 
{
    int V1, V2;
    double similitud;

    //Constructora

    public Aresta(int v1, int v2, double sim)
    {
        this.V1 = v1;
        this.V2 = v2;
        this.similitud = sim;
    }
}
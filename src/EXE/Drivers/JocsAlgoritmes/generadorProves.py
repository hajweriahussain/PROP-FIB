import random

nombre_archivo = "prova10.txt" # -------Escriu aquí el nom de l'arxiu que vulguis-------

num_productes = 10 # -------Escriu aquí el número de nodes que vulguis-------


productos = [f"{i + 1} {chr(65 + (i % 26))}" for i in range(num_productes)]

matriu_similituds = [[0.0 if i == j else round(random.uniform(0, 1), 5) for j in range(num_productes)] for i in range(num_productes)]

for i in range(num_productes):
    for j in range(i + 1, num_productes):
        matriu_similituds[j][i] = matriu_similituds[i][j]

with open(nombre_archivo, "w") as file:
    file.write(f"Número de productes: {num_productes}\n")
    file.write("\n".join(productos) + "\n")
    file.write("Matriu similituds:\n")
    
    for fila in matriu_similituds:
        file.write(" ".join(f"{x:.5f}" for x in fila) + "\n")

print(f"Arxiu '{nombre_archivo}' generat amb èxit :) .")

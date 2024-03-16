from matplotlib import pyplot as plt
from matplotlib.ticker import ScalarFormatter
import numpy as np

def representation_graphique(ordonnee, nom_fichier, xlabe='', ylabe='', titre='', label='', plot=False):
    abscisse = np.arange(len(ordonnee))
    fig, ax = plt.subplots()
    ax.plot(abscisse, ordonnee, label=label)

    ax.set_xlabel(xlabe)
    ax.set_ylabel(ylabe)
    ax.set_title(titre)
    ax.legend()

    # Sauvegarder le graphique
    plt.savefig("src/experimentation/resultats/" + nom_fichier)

    if plot:
        plt.show()

def bar_chart(values, title, xlabel, ylabel, savefile=None, plot=False):
    x = np.arange(len(values))
    plt.bar(x, values)
    plt.xlabel(xlabel)
    plt.ylabel(ylabel)
    plt.title(title)
    # plt.xticks(x, labels)
    
    if savefile is None:
        savefile = title
    
    if savefile:
        plt.savefig("src/experimentation/resultats/" + savefile)
    
    if plot:
        plt.show()
         
def treat_from_file(file_name):
    list_of_values = []
    with open(file_name, 'r') as file:
        for line in file:
            # Divise la ligne en deux valeurs (supposées être des doubles) et crée un tuple
            values = line.split()
            if len(values) == 2:
                try:
                    tuple_values = (float(values[0]), float(values[1]))
                    list_of_values.append(tuple_values)
                except ValueError:
                    print("Erreur de conversion en float pour la ligne:", line)
    return list_of_values

if __name__ == '__main__':
    liste = treat_from_file("src/experimentation/resultats/times")
    ecartype = treat_from_file("src/experimentation/resultats/ecartypes")
    # Representation courbe de time
    representation_graphique([x[0] for x in liste] , "timeNaife.png", "x", "y", "Naif", "Time", False)
    representation_graphique([x[1] for x in liste] , "timeWelzl.png", "x", "y", "Welzl", "Time", False)
    # Representaion courbe ecart
    representation_graphique([x[0] for x in ecartype] , "ecartypeNaife.png", "x", "y", "Naif", "Ecart-Type", False)
    representation_graphique([x[1] for x in ecartype] , "ecartypeWelzl.png", "x", "y", "Welzl", "Ecart-Type", False)
    # Representation Diagram Baton time
    bar_chart([x[0] for x in liste], "Moyenne Naif", "x", "y", "Naif", True)
    bar_chart([x[1] for x in liste], "Moyenne Welzl", "x", "y", "Welzl", True)
    # Representation Diagram Baton ecart
    bar_chart([x[0] for x in ecartype], "Ecart type Naif", "x", "y", "ecartype", True)
    bar_chart([x[1] for x in ecartype], "Ecart type Welzl", "x", "y", "ecartype", True)
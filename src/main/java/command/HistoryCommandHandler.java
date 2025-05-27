package command;

import java.util.LinkedList;

public class HistoryCommandHandler implements CommandHandler {

    private int maxHistoryLength = 100;
    private final LinkedList<Command> history = new LinkedList<>(); //storia dei do
    private final LinkedList<Command> redoList = new LinkedList<>(); //storia degli undo


    //Costruttore di default: lunghezza pari a 100
    public HistoryCommandHandler() {
        this(100);
    }

    //Costruttore con lunghezza massima specificata
    public HistoryCommandHandler(int maxHistoryLength) {
        if (maxHistoryLength < 0)
            throw new IllegalArgumentException();
        this.maxHistoryLength = maxHistoryLength;
    }

    public boolean handle(Command cmd) {
        boolean ret = true;
        if (cmd.execute()) {
            addToHistory(cmd);
            //se il comando va a buon fine viene salvato nella cronologia per poter essere annullato in futuro
        } else {
            history.clear();
            ret = false;//se il comando non va a buon fine si elimina tutta la cronologia per evitare di fare undo a partire da uno stato inconsistente
        }
        if (redoList.size() > 0)
            redoList.clear();
        return ret;
    }

    public void redo() {
        if (redoList.size() > 0) {
            Command redoCmd = redoList.removeFirst();
            redoCmd.execute();
            //history.addFirst(redoCmd);
            addToHistory(redoCmd);

        }
    }

    public void undo() {
        if (history.size() > 0) {
            Command undoCmd = history.removeFirst();
            undoCmd.undo();
            redoList.addFirst(undoCmd);
        }
    }

    private void addToHistory(Command cmd) {
        history.addFirst(cmd);
        if (history.size() > maxHistoryLength) {
            history.removeLast();
        }

    }


}

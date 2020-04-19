/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import ucb.gui2.TopLevel;
import ucb.gui2.LayoutSpec;

import java.awt.Dimension;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import java.io.InputStream;
import java.io.IOException;
import java.io.StringWriter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static loa.Piece.*;

/** The GUI controller for a LOA board and buttons.
 *  @author Ayela Chughtai
 */
class GUI extends TopLevel implements View, Reporter {

    /** Minimum size of board in pixels. */
    private static final int MIN_SIZE = 500;

    /** Size of pane used to contain help text. */
    static final Dimension TEXT_BOX_SIZE = new Dimension(500, 700);

    /** Resource name of "About" message. */
    static final String ABOUT_TEXT = "loa/About.html";

    /** Resource name of Loa help text. */
    static final String HELP_TEXT = "loa/Help.html";

    /** Resource name of Loa help text. */
    static final String HELP_TEXT2 = "loa/HelpText.txt";

    /** A new window with given TITLE providing a view of a Loa board. */
    GUI(String title) {
        super(title, true);
        addMenuButton("Game->New", this::newGame);
        addSeparator("Game");
        addMenuButton("Game->Quit", this::quit);
        addSeparator("Game");
        addMenuButton("Game->Dump", this::dump);
        addSeparator("Game");
        addMenuButton("Game->Undo", this::undo);
        addMenuButton("Help->About", (s) -> displayText("About", ABOUT_TEXT));
        addMenuButton("Help->?", this::help);
        addMenuButton("Help->Help", this::help2);
        addMenuButton("Help->Rules", (s) -> displayText("Help", HELP_TEXT));
        addMenuButton("Settings->Seed", this::newSeed);
        addMenuButton("Settings->Limit", this::newLimit);
        addMenuButton("Settings->Set", this::set);
        addMenuButton("Player->auto black", this::autoBlack);
        addMenuButton("Player->auto white", this::autoWhite);
        addMenuButton("Player->manual black", this::manualBlack);
        addMenuButton("Player->manual white", this::manualWhite);

        _widget = new BoardWidget(_pendingCommands);
        add(_widget,
            new LayoutSpec("y", 1,
                           "height", 1,
                           "width", 3));
        addLabel("To move: White", "CurrentTurn",
                 new LayoutSpec("x", 0, "y", 0,
                                "height", 1,
                                "width", 3));
        addLabel("Black: Manual\t\t\t\tWhite: Auto", "state",
                new LayoutSpec("x", 0,
                        "y", 8 * BoardWidget.SQUARE_SIDE,
                        "height", 1,
                        "width", 3));
    }
    /**
     * Turn into machine player.
     * @param str WP to turn.
     */
    private void autoWhite(String str) {
        _pendingCommands.offer("auto white");
    }
    /**
     * Turn into machine player.
     * @param str BP to turn.
     */
    private void autoBlack(String str) {
        _pendingCommands.offer("auto black");
    }

    /**
     * Turn into manual player.
     * @param str BP to turn.
     */
    private void manualBlack(String str) {
        _pendingCommands.offer("manual black");
    }

    /**
     * Turn into manual player.
     * @param str WP to turn.
     */
    private void manualWhite(String str) {
        _pendingCommands.offer("manual white");
    }

    /**
     * Dump the info of the board.
     * @param dump .
     */
    private void dump(String dump) {
        _pendingCommands.offer("dump");
    }

    /**
     * Help.
     * @param help .
     */
    private void help(String help) {
        _pendingCommands.offer("?");
    }

    /**
     * Help.
     * @param help .
     */
    private void help2(String help) {
        _pendingCommands.offer("help");
    }

    /** Pattern describing the 'limit' command's arguments. */
    private static final Pattern LIMIT_PATN =
            Pattern.compile("\\s*(\\d{1,18})\\s*$");

    /**
     * Set new move limit.
     * @param i number.
     */
    private void newLimit(String i) {
        String response =
                getTextInput("Enter new limit.", "New limit",  "plain", "");
        if (response != null) {
            Matcher mat = LIMIT_PATN.matcher(response);
            if (mat.matches()) {
                _pendingCommands.offer(String.format("limit %s", mat.group(1)));
            } else {
                showMessage("Enter an integral limit value.", "Error", "error");
            }
        }
    }

    /**
     * Set new position.
     * @param set string.
     */
    private void set(String set) {
        _pendingCommands.offer(set);
    }
    /** Response to "Quit" button click. */
    private void quit(String dummy) {
        _pendingCommands.offer("quit");
    }

    /** Response to "New Game" button click. */
    private void newGame(String dummy) {
        _pendingCommands.offer("new");
    }

    /** Response to "Undo" button click. */
    private void undo(String dummy) {
        _pendingCommands.offer("undo");
    }

    /** Pattern describing the 'seed' command's arguments. */
    private static final Pattern SEED_PATN =
            Pattern.compile("\\s*(-?\\d{1,18})\\s*$");

    /** Response to "Seed" button click. */
    private void newSeed(String dummy) {
        String response =
                getTextInput("Enter new random seed.", "New seed",
                        "plain", "");
        if (response != null) {
            Matcher mat = SEED_PATN.matcher(response);
            if (mat.matches()) {
                _pendingCommands.offer(String.format("SEED %s", mat.group(1)));
            } else {
                showMessage("Enter an integral seed value.", "Error", "error");
            }
        }
    }

    /** Return the next command from our widget, waiting for it as necessary.
     *  The BoardWidget uses _pendingCommands to queue up moves that it
     *  receives.  Thie class uses _pendingCommands to queue up commands that
     *  are generated by clicking on menu items. */
    String readCommand() {
        try {
            _widget.setMoveCollection(true);
            String cmnd = _pendingCommands.take();
            _widget.setMoveCollection(false);
            return cmnd;
        } catch (InterruptedException excp) {
            throw new Error("unexpected interrupt");
        }
    }

    @Override
    public void update(Game controller) {
        Board board = controller.getBoard();

        _widget.update(board);
        if (board.winner() != null) {
            setLabel("CurrentTurn",
                     String.format("Winner: %s",
                                   board.winner().fullName()));
        } else {
            setLabel("CurrentTurn",
                     String.format("To move: %s", board.turn().fullName()));
        }

        boolean manualWhite = controller.manualWhite(),
            manualBlack = controller.manualBlack();
        display(true);

        setLabel("state", String.format("Black: %s\t\t\t\tWhite: %s",
                manualBlack ? "Manual   " : "Auto   ",
                manualWhite ? "Manual" : "Auto"));
    }

    /** Display text in resource named TEXTRESOURCE in a new window titled
     *  TITLE. */
    private void displayText(String title, String textResource) {
        /* Implementation note: It would have been more convenient to avoid
         * having to read the resource and simply use dispPane.setPage on the
         * resource's URL.  However, we wanted to use this application with
         * a nonstandard ClassLoader, and arranging for straight Java to
         * understand non-standard URLS that access such a ClassLoader turns
         * out to be a bit more trouble than it's worth. */
        JFrame frame = new JFrame(title);
        JEditorPane dispPane = new JEditorPane();
        dispPane.setEditable(false);
        dispPane.setContentType("text/html");
        InputStream resource =
            GUI.class.getClassLoader().getResourceAsStream(textResource);
        StringWriter text = new StringWriter();
        try {
            while (true) {
                int c = resource.read();
                if (c < 0) {
                    dispPane.setText(text.toString());
                    break;
                }
                text.write(c);
            }
        } catch (IOException e) {
            return;
        }
        JScrollPane scroller = new JScrollPane(dispPane);
        scroller.setVerticalScrollBarPolicy(scroller.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setPreferredSize(TEXT_BOX_SIZE);
        frame.add(scroller);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void reportError(String fmt, Object... args) {
        showMessage(String.format(fmt, args), "Loa Error", "error");
    }

    @Override
    public void reportNote(String fmt, Object... args) {
        showMessage(String.format(fmt, args), "Loa Message", "information");
    }

    @Override
    public void reportMove(Move unused) {
    }

    /** The board widget. */
    private BoardWidget _widget;

    /** Queue of pending commands resulting from menu clicks and moves on the
     *  board.  We use a blocking queue because the responses to clicks
     *  on the board and on menus happen in parallel to the methods that
     *  call readCommand, which therefore needs to wait for clicks to happen. */
    private ArrayBlockingQueue<String> _pendingCommands =
        new ArrayBlockingQueue<>(5);

}

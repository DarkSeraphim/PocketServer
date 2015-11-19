package com.pocketserver.impl;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;

import com.pocketserver.impl.gui.ConsoleWindow;

public class PSPrintStream extends PrintStream {

    private ConsoleWindow window;
    private boolean isErr;
    
    PSPrintStream(ConsoleWindow window, boolean isErr, OutputStream out, boolean b) {
        super(out, b);
        this.window = window;
        this.isErr = isErr;
    }
    
    @Override
    public void write(int b) {
        super.write(b);
        window.write(isErr, String.valueOf(b & 0xFF));
    }

    @Override
    public void write(byte buf[], int off, int len) {
        super.write(buf, off, len);
        for (int i = 0; i < len; i++)
            if (off+i < buf.length)
                window.write(isErr, String.valueOf(buf[off+i]));
            else
                break;
    }

    @Override
    public void print(boolean b) {
        super.print(b);
        window.write(isErr, String.valueOf(b));
    }

    @Override
    public void print(char c) {
        super.print(c);
        window.write(isErr, c);
    }

    @Override
    public void print(int i) {
        super.print(i);
        window.write(isErr, String.valueOf(i));
    }

    @Override
    public void print(long l) {
        super.print(l);
        window.write(isErr, String.valueOf(l));
    }

    @Override
    public void print(float f) {
        super.print(f);
        window.write(isErr, String.valueOf(f));
    }

    @Override
    public void print(double d) {
        super.print(d);
        window.write(isErr, String.valueOf(d));
    }

    @Override
    public void print(char s[]) {
        super.print(s);
        window.write(isErr, String.valueOf(s));
    }

    @Override
    public void print(String s) {
        super.print(s);
        window.write(isErr, s);
    }

    @Override
    public void print(Object obj) {
        super.print(obj);
        window.write(isErr, String.valueOf(obj));
    }

    @Override
    public void println() {
        super.println();
        window.write(isErr, "\n");
    }

    @Override
    public void println(boolean x) {
        super.println(x);
        window.write(isErr, "\n");
    }

    @Override
    public void println(char x) {
        super.println(x);
        window.write(isErr, "\n");
    }

    @Override
    public void println(int x) {
        super.println(x);
        window.write(isErr, "\n");
    }

    @Override
    public void println(long x) {
        super.println(x);
        window.write(isErr, "\n");
    }

    @Override
    public void println(float x) {
        super.println(x);
        window.write(isErr, "\n");
    }

    @Override
    public void println(double x) {
        super.println(x);
        window.write(isErr, "\n");
    }

    @Override
    public void println(char x[]) {
        super.println(x);
        window.write(isErr, "\n");
    }

    @Override
    public void println(String x) {
        super.println(x);
        window.write(isErr, "\n");
    }

    @Override
    public void println(Object x) {
        super.println(x);
        window.write(isErr, "\n");
    }

    @Override
    public PrintStream printf(String format, Object ... args) {
        try {
            window.write(isErr, String.format(format, args));
        } catch (Exception ex) {}
        return super.printf(format, args);
    }

    @Override
    public PrintStream printf(Locale l, String format, Object ... args) {
        try {
            window.write(isErr, String.format(l, format, args));
        } catch (Exception ex) {}
        return super.printf(l, format, args);
    }

    @Override
    public PrintStream format(String format, Object ... args) {
        try {
            window.write(isErr, String.format(format, args));
        } catch (Exception ex) {}
        return super.format(format, args);
    }

    @Override
    public PrintStream format(Locale l, String format, Object ... args) {
        try {
            window.write(isErr, String.format(l, format, args));
        } catch (Exception ex) {}
        return super.format(l, format, args);
    }

    @Override
    public PrintStream append(CharSequence csq) {
        super.append(csq);
        window.write(isErr, String.valueOf(csq));
        return this;
    }

    @Override
    public PrintStream append(CharSequence csq, int start, int end) {
        super.append(csq, start, end);
        window.write(isErr, String.valueOf(csq.subSequence(start, end)));
        return this;
    }

    @Override
    public PrintStream append(char c) {
        super.append(c);
        window.write(isErr, c);
        return this;
    }

}

package com.ccis.seafoodtrans.scan_barcode_with_java_code;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import kotlin.jvm.internal.Intrinsics;

/**
 * Wraps an existing executor to provide a [.shutdown] method that allows subsequent
 * cancellation of submitted runnables.
 */
public final class ScopedExecutor implements Executor {
    private final AtomicBoolean shutdown;
    private final Executor executor;

    public void execute(@NotNull final Runnable command) {
        Intrinsics.checkParameterIsNotNull(command, "command");
        if (!this.shutdown.get()) {
            this.executor.execute((Runnable)(new Runnable() {
                public final void run() {
                    if (!ScopedExecutor.this.shutdown.get()) {
                        command.run();
                    }
                }
            }));
        }
    }

    public final void shutdown() {
        this.shutdown.set(true);
    }

    public ScopedExecutor(@NotNull Executor executor) {
        super();
        this.executor = executor;
        this.shutdown = new AtomicBoolean();
    }
}


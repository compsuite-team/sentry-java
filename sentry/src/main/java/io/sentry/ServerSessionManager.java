package io.sentry;

import java.util.Date;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ServerSessionManager implements SessionTracker, SessionUpdater {
  private @Nullable Status status;
  private final @NotNull SessionAggregates sessionAggregates;

  ServerSessionManager(final @NotNull SessionAggregates sessionAggregates) {
    this.sessionAggregates = sessionAggregates;
  }

  @Override
  public void startSession() {
    status = null;
  }

  @Override
  @SuppressWarnings("JavaUtilDate")
  public void endSession() {
    // add to aggregates
    if (status == null) {
      status = Status.Exited;
    }
    sessionAggregates.addSession(new Date(), status);
  }

  @Override
  @SuppressWarnings("JavaUtilDate")
  public @Nullable Session updateSessionData(
      @NotNull SentryEvent event, @Nullable Object hint, @Nullable Scope scope) {
    if (status != Status.Crashed) {
      status = event.isCrashed() ? Status.Crashed : Status.Errored;
    }
    return null;
  }

  enum Status {
    Exited,
    Errored,
    Crashed;
  }
}

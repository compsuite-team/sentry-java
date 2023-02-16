package io.sentry;

import io.sentry.protocol.SentryPackage;
import io.sentry.util.Objects;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class SentryIntegrationPackageStorage {
  private static volatile @Nullable SentryIntegrationPackageStorage INSTANCE;

  public static @NotNull SentryIntegrationPackageStorage getInstance() {
    if (INSTANCE == null) {
      synchronized (SentryIntegrationPackageStorage.class) {
        if (INSTANCE == null) {
          INSTANCE = new SentryIntegrationPackageStorage();
        }
      }
    }

    return INSTANCE;
  }

  /**
   * List of integrations that are enabled in the SDK. _Optional._
   *
   * <p>The list should have all enabled integrations, including default integrations. Default
   * integrations are included because different SDK releases may contain different default
   * integrations.
   */
  private final Set<String> integrations = new CopyOnWriteArraySet<>();

  /**
   * List of installed and loaded SDK packages. _Optional._
   *
   * <p>A list of packages that were installed as part of this SDK or the activated integrations.
   * Each package consists of a name in the format `source:identifier` and `version`. If the source
   * is a Git repository, the `source` should be `git`, the identifier should be a checkout link and
   * the version should be a Git reference (branch, tag or SHA).
   */
  private final Set<SentryPackage> packages = new CopyOnWriteArraySet<>();

  private SentryIntegrationPackageStorage() {}

  public void addIntegration(final @NotNull String integration) {
    Objects.requireNonNull(integration, "integration is required.");
    integrations.add(integration);
  }

  public @NotNull List<String> getIntegrations() {
    return new CopyOnWriteArrayList<>(integrations);
  }

  public void addPackage(final @NotNull String name, final @NotNull String version) {
    Objects.requireNonNull(name, "name is required.");
    Objects.requireNonNull(version, "version is required.");

    SentryPackage newPackage = new SentryPackage(name, version);
    packages.add(newPackage);
  }

  public @NotNull List<SentryPackage> getPackages() {
    return new CopyOnWriteArrayList<>(packages);
  }
}

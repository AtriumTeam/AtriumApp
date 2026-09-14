# R8 rules for the release build.
#
# Hilt, Compose, kotlinx-coroutines and AndroidX ship their own consumer rules,
# so nothing is needed for them here. Rules are added when a real dependency
# requires them, not preemptively.

# Keep line numbers so release crash reports stay readable.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

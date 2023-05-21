# This dockerfile is intended to be built and ran in the project directory. When running, mount the directory with:
# docker run -itdv .:/workspaces <image-name>
FROM wpilib/ubuntu-base:22.04

ENV SHELL /bin/bash
ARG WORKSPACE=/workspace
VOLUME $WORKSPACE
WORKDIR $WORKSPACE
# Let gradle build once, creating a cache at ~/.gradle to make cold starts faster
COPY . .
RUN chmod +x gradlew && ./gradlew build --build-cache
# Cleanup workspace for volume that will be mounted at runtime
RUN rm -rf *
# git will otherwise see $WORKSPACE to be under dubious ownership, preventing use of git commands
RUN git config --global --add safe.directory $WORKSPACE
ENTRYPOINT /bin/bash
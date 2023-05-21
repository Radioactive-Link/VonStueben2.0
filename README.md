# VonStueben2.0
[![CI](https://github.com/Radioactive-Link/VonJava/actions/workflows/main.yml/badge.svg)](https://github.com/Radioactive-Link/VonJava/actions/workflows/main.yml)

VonStueben but in java (and kotlin)

***

# Docker
This project has support for building in a reproducible docker container.
The following is a guide to using the Dockerfile found this repo.
To follow along, make sure that you have the repo cloned:
```shell
git clone https://github.com/Radioactive-Link/VonStueben2.0.git
```
Additionally, make sure you have the following installed:
* Docker
  * [Windows Install](https://docs.docker.com/desktop/install/windows-install/)
  * [Linux Install](https://docs.docker.com/desktop/install/linux-install/)
  * [Mac Install](https://docs.docker.com/desktop/install/mac-install/)

## Building the image
To build the image, use:
```shell
# Assuming current directory is this repo
docker build -t wpilib-project .
```
This will create an image named "wpilib-project" using the Dockerfile in this repo.
The Dockerfile builds the code in order to have a cache in ~/.gradle so that container's cold runs
are not as slow, this does increase the image build time though.

## Running the container
To run a container from the image, use:
```shell
# Assuming current directory is this repo
docker run -itdv .:/workspace --name stueben wpilib-project
```
This creates a running container with the name "stueben".
The "-v" argument uses the syntax <host-dir>:<container-dir>.
The point is to mount a host directory to the container as a volume, essentially sharing the directory.
In this case, we are mounting the repo in order to access its code locally and in the container for building.
In the Dockerfile, the "COPY . ." command *could* be used in order to access the code for building,
but I wanted the container to be deletable at any time without losing uncommitted/pushed progress.
This is exactly what is accomplished by mounting a volume.

## Building in the container
With the container started, you can now build the robot code inside of it. Do that with:
```shell
./gradlew build --build-cache
```

## Troubleshooting

### Commandline errors
<u>When running ./gradlew</u>
```shell
bash: gradlew: command not found...
```
Make sure that the gradlew file is executable with:
```shell
chmod +x gradlew
```
And retry the command

<u>When running git commands in the container, git may complain about various things.</u>
To resolve these issues, just follow git's suggestion that it outputted.

### Other
<u>Docker container stops immediately after start</u>
  
This issue can be caused when running the container for the first time through an IDE
or Docker Desktop. Make sure the container is started with the command mentioned above:
```shell
docker run -itdv .:/workspace --name stueben wpilib-project
```
It should be ok to restart the container from an IDE or Docker Desktop after the initial
launch from the commandline.

## Workflow
A general workflow looks like this:
```shell
# Initial setup
git clone https://github.com/Radioactive-Link/VonStueben2.0.git
cd VonStueben2.0
docker build -t wpilib-project .

# General Workflow
cd /path/to/VonStueben2.0
docker run -itdv .:/workspace --name stueben wpilib-project

# OR, to build and run, do:
docker-compose up -d
# Note: Docker compose needs to be installed (comes with Docker Desktop if installed on mac or windows)
# Will build for the first run only, then will use the image from the initial build for
# subsequent builds (unless the image is manually deleted in which case it will make a new one)
```
From there, attach a terminal from the container in your IDE, view it in Docker Desktop,
or attach it via commandline with:
```shell
docker attach stueben
```
Once in the container's terminal, all gradle tasks can be run. The most common is "build" which builds the code:
```shell
./gradlew build --build-cache
```
Since the local repo is mounted to the container as a volume, I highly recommend using git on the host machine.
This avoids issues from not having git configured in the container.
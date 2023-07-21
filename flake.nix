# Note: Gradle needs sudo permission, at least on nixos.
# I think this is because nix isolates programs, and having default access,
# in this case to the local directory ~/.gradle, makes things less pure.
{
  description = "Nix dev-shell configuration in order to build with gradle";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/master";
  };

  outputs = inputs@{ self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem 
      (system: 
        let
          pkgs = inputs.nixpkgs.legacyPackages.${system};
        in
        {
          packages.default = pkgs.hello;
          # Run "nix develop" in order to use the dev shell
          devShells.default = pkgs.mkShell {
            name = "wpilib";
            nativeBuildInputs = [ pkgs.jdk17 pkgs.gradle ];
            shellHook = ''
              export JAVA_HOME=${pkgs.jdk17}
              alias tasks="sudo gradle tasks"
              alias build="sudo gradle build --build-cache --no-daemon"
              alias test="sudo gradle test"
              alias deploy="sudo gradle deploy --no-daemon"
            ''; 
          };
        }
      );
}
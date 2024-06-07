{
  description = "scala-workshop";

  inputs = {
    nixpkgs.url = github:nixos/nixpkgs/nixpkgs-unstable;
    flake-utils.url = github:numtide/flake-utils;
  };

  outputs = { self, nixpkgs, flake-utils, ... }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
          overlays = [
            (f: p: {
              scala-cli = p.scala-cli.override { jre = p.temurin-bin-21; };
            })
          ];
        };

        bleep-flake = import ./bleep.nix {
          inherit flake-utils;
          inherit pkgs;
        };

        bleep = bleep-flake.defaultPackage.${system};

        jdk = pkgs.temurin-bin-21;

        jvmInputs = with pkgs; [
          scalafmt
          coursier
          jdk
          bleep
          scala-cli
          zlib
        ];

        jvmHook = ''
          export JAVA_HOME="${jdk}"
        '';

      in
      {
        devShells.default = pkgs.mkShell {
          name = "scala-workshop-dev-shell";
          buildInputs = jvmInputs; 
          LD_LIBRARY_PATH = pkgs.lib.makeLibraryPath jvmInputs;
          shellHook = jvmHook;
        };
      }
    );

}


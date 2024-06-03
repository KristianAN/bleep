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
              scala-cli = p.scala-cli.override { jre = p.graalvm-ce; };
            })
          ];
        };

        bleep-flake = import ./bleep.nix {
          inherit flake-utils;
          inherit pkgs;
        };

        bleep = bleep-flake.defaultPackage.${system};

        jdk = pkgs.graalvm-ce;

        jvmInputs = with pkgs; [
          scalafmt
          coursier
          jdk
          bleep
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


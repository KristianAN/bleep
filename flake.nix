{
  description = "bleep";

  inputs = {
     nixpkgs.url = "github:NixOS/nixpkgs";
     bleepSrc.url = "path:/home/kristian/src/bleep-flake"; # The bleep flake
     flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, bleepSrc, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let pkgs = import nixpkgs { inherit system; };
          bleep = bleepSrc.defaultPackage.${system}; # Your bleep system binary

          jdk = pkgs.jdk17_headless;

          jvmHook = ''
            JAVA_HOME="${jdk}"
          '';
      in {
        devShell = pkgs.mkShell rec {
          buildInputs = [
            bleep # Bleep bleep
            jdk
          ];
          shellHook = jvmHook;          
        };
      });
}

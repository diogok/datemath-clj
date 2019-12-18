

.PHONY: test
test:
	clj -A:test

.PHONY: jar
jar:
	clojure -Sdeps '{:deps {pack/pack.alpha {:git/url "https://github.com/juxt/pack.alpha.git" :sha "2769a6224bfb938e777906ea311b3daf7d2220f5"}}}' -m mach.pack.alpha.skinny --no-libs --project-path datemath.jar

.PHONY: deploy
deploy:
	mvn deploy:deploy-file -Dfile=datemath.jar -DrepositoryId=clojars -Durl=https://clojars.org/repo -DpomFile=pom.xml


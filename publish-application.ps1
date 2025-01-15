cls

$deployFolder = "docs"
$npmRoot = "githubpages"
$distFolder = "$npmRoot\dist"
$commitMessage = "publish"
$gitBranchLocal = "main"
$gitBranchRemote = "main"
$gitRepoRemote = "https://github.com/jelle-de-jong-hu/GitHubPages.git"

Remove-Item ".\$deployFolder\" -Force -Recurse -ErrorAction SilentlyContinue

npm run build --prefix "$npmRoot"

Move-Item "$distFolder" "$deployFolder" -Force
New-Item -Path "$deployFolder\.nojekyll" -Force

git add "$deployFolder/*"
git commit -m "$commitMessage"
git push -f "$gitRepoRemote" "$($gitBranchLocal):$($gitBranchRemote)"
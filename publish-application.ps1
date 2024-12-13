cls

$deployFolder = "docs"
$npmRoot = "githubpages"
$distFolder = "$npmRoot\dist"

Remove-Item ".\$deployFolder\" -Force -Recurse

npm run build --prefix $npmRoot --output-path="$docs"

Move-Item "$distFolder" $deployFolder -Force
New-Item -Path "$deployFolder\.nojekyll" -Force
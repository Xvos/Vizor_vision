task :default => [ :native_build ]

task :clean do |t|
	sh("rm -rfv libs")
end

task :native_build do |t|
	ndk_folder = "#{ENV['ANDROID_NDK']}"
	build_exe = ndk_folder + "/ndk-build NDK_DEBUG=0"

	sh(build_exe)
end

task :rebuild do |t|
	Rake::Task["clean"].execute
	Rake::Task["native_build"].execute
end

use elgato_streamdeck::new_hidapi;
use hidapi::HidApi;
use jni::JNIEnv;
use jni::objects::{JClass};
use jni::sys::{jlong};

#[no_mangle]
pub extern "C" fn Java_org_streamduck_elgato_1streamdeck_HidApi_newHidApi<'local>(
    mut env: JNIEnv<'local>,
    _class: JClass<'local>
) -> jlong {
    let hidresult = new_hidapi();

    match hidresult {
        Ok(hidapi) => {
            let boxed = Box::new(hidapi);
            Box::into_raw(boxed) as jlong
        }
        Err(err) => {
            let _ = env.throw_new("java/lang/RuntimeException", err.to_string());
            0
        }
    }
}

#[no_mangle]
pub unsafe extern "C" fn Java_org_streamduck_elgato_1streamdeck_HidApi_freeHidApi<'local>(
    mut _env: JNIEnv<'local>,
    _class: JClass<'local>,
    address: jlong
) {
    let _ = Box::from_raw(address as *mut HidApi);
}